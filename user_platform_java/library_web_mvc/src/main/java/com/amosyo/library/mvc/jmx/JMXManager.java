package com.amosyo.library.mvc.jmx;

import com.amosyo.library.mvc.function.ThrowableAction;
import com.amosyo.library.mvc.function.ThrowableCallable;
import com.amosyo.library.mvc.jmx.annotation.JMXManagedAttribute;
import com.amosyo.library.mvc.jmx.annotation.JMXManagedOperation;
import com.amosyo.library.mvc.jmx.annotation.JMXManagedResource;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class JMXManager {

    private static final Logger logger = Logger.getLogger(JMXManager.class.getName());
    private static final Set<Class<?>> CLASSED_MBEAN_SUPPORTED = Sets.newHashSet(String.class, Integer.class, int.class, Float.class, float.class, Double.class, double.class, Boolean.class, boolean.class);
    private static final Set<Class<?>> CLASSED_BOOLEANS = Sets.newHashSet(Boolean.class, boolean.class);

    private final MBeanServer mBeanServer;

    private JMXManager() {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    private static final class Holder {
        private static final JMXManager MANAGER = new JMXManager();
    }

    public static JMXManager getInstance() {
        return Holder.MANAGER;
    }

    public void tryRegisterPojo(@NonNull final Object object) {
        requireNonNull(object, "object");
        if (!object.getClass().isAnnotationPresent(JMXManagedResource.class)) {
            logger.log(Level.WARNING, "Warning to Skip Register Pojo to MBean Service, cause it is not annotation present to JMXManagedResource ");
            return;
        }
        ThrowableAction.execute(() -> {
            final ModelMBeanInfo modelMBeanInfo = getModelMBeanInfo(object);
            final RequiredModelMBean requiredModelMBean = new RequiredModelMBean(modelMBeanInfo);
            requiredModelMBean.setManagedResource(object, "ObjectReference");
            final String domain = mBeanServer.getDefaultDomain();
            final String objName = Optional.of(object.getClass().getAnnotation(JMXManagedResource.class).objectName())
                    .filter(StringUtils::isNotBlank)
                    .orElseGet(() -> String.format("%s:type=%s", domain, object.getClass().getSimpleName()));
            final ObjectName objectName = new ObjectName(objName);
            mBeanServer.registerMBean(requiredModelMBean, objectName);
        });
    }

    @NonNull
    private ModelMBeanInfo getModelMBeanInfo(@NonNull final Object object) {

        return ThrowableCallable.call(() -> {

            final BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());

            final List<ModelMBeanAttributeInfo> attributeInfoList = Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(JMXManagedAttribute.class))
                    .map(field -> doConvertField2ModelMBeanAttributeInfo(field, beanInfo))
                    .collect(Collectors.toList());

            final List<ModelMBeanOperationInfo> operationInfoList = Arrays.stream(object.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(JMXManagedOperation.class))
                    .map(method -> doConvertMethod2ModelModelMBeanOperationInfo(method, object))
                    .collect(Collectors.toList());

            return new ModelMBeanInfoSupport(RequiredModelMBean.class.getName(),
                    "ModelMBeanInfoSupport Dynamic MBean",
                    attributeInfoList.toArray(new ModelMBeanAttributeInfo[]{}),
                    null,
                    operationInfoList.toArray(new ModelMBeanOperationInfo[]{}),
                    null,
                    null
            );
        }, false, null);
    }

    @NonNull
    private ModelMBeanAttributeInfo doConvertField2ModelMBeanAttributeInfo(@NonNull final Field field, @NonNull final BeanInfo beanInfo) {

        final Class<?> filedType = field.getType();
        if (!CLASSED_MBEAN_SUPPORTED.contains(filedType)) {
            throw new IllegalArgumentException("Error to Filed UnSupport convert to MBeanAttributeInfo, it is " + field);
        }

        final String fieldName = field.getName();
        final JMXManagedAttribute filedAnnotation = field.getAnnotation(JMXManagedAttribute.class);
        final PropertyDescriptor fieldPropertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors()).filter(descriptor -> Objects.equals(descriptor.getName(), fieldName)).findFirst().orElseThrow(() -> new IllegalArgumentException("Error to Not Found Descriptor, it is " + fieldName));
        final boolean isReadable = !isNull(fieldPropertyDescriptor.getReadMethod());
        final boolean isWritable = !isNull(fieldPropertyDescriptor.getReadMethod());
        final boolean isBoolean = CLASSED_BOOLEANS.contains(filedType);

        final Descriptor descriptor = new DescriptorSupport() {{
            setField("name", fieldName);
            setField("descriptorType", "attribute");
            setField("displayName", fieldPropertyDescriptor.getDisplayName());
            setField("getMethod", fieldPropertyDescriptor.getReadMethod().getName());
            setField("setMethod", fieldPropertyDescriptor.getWriteMethod().getName());

        }};

        return new ModelMBeanAttributeInfo(
                fieldName, filedType.getName(), filedAnnotation.description(), isReadable, isWritable, isBoolean, descriptor
        );

    }

    @NonNull
    private ModelMBeanOperationInfo doConvertMethod2ModelModelMBeanOperationInfo(@NonNull final Method method, @NonNull final Object object) {

        final String methodName = method.getName();

        final String descriptorName = String.format("name=%s", methodName);
        final String descriptorType = "descriptorType=operation";
        final String descriptorClass = "class=" + object.getClass().getName();
        final String descriptorRole = "role=operation";

        final Descriptor descriptor = new DescriptorSupport(descriptorName, descriptorType, descriptorClass, descriptorRole);

        final JMXManagedOperation managedOperation = method.getAnnotation(JMXManagedOperation.class);
        final List<MBeanParameterInfo> parameterInfos = Arrays.stream(method.getParameters())
                .map(parameter -> new MBeanParameterInfo(parameter.getName(), parameter.getType().getName(), ""))
                .collect(Collectors.toList());
        final String typeName = method.getReturnType().getName();

        return new ModelMBeanOperationInfo(
                methodName,
                managedOperation.description(),
                parameterInfos.toArray(new MBeanParameterInfo[]{}),
                typeName,
                ModelMBeanOperationInfo.ACTION_INFO,
                descriptor
        );
    }
}
