package site.patrickshao.admin.biz.repository;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.utils.ReflectUtils;

import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@Component
public class RepoBeanDefinitionRegister implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        List<Class<?>> entityClasses = ReflectUtils
                .scan("site.patrickshao.admin.common.entity.po");
        for (Class<?> entityClass : entityClasses) {
            if (entityClass.getAnnotation(GenerateRepository.class) == null) continue;
            Class<?> repoClass = generateRepoClass(entityClass);
            RootBeanDefinition definition = new RootBeanDefinition(repoClass);
            registry.registerBeanDefinition(repoClass.getName(), definition);

        }
    }

    private Class<?> generateRepoClass(Class<?> entityClazz) {
        Class<?> repoSuperClassType = DefaultRepository.class;
        TypeDescription.Generic generic = TypeDescription.Generic
                .Builder
                .parameterizedType(repoSuperClassType, entityClazz)
                .build();
        Class<?> repoClazz = new ByteBuddy()
                .subclass(generic)
                .name("site.patrickshao.admin.biz.repository." + entityClazz.getCanonicalName() + "Repository")
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();
        ReflectUtils.registerClass(repoClazz);
        try {
            ReflectUtils.forName(repoClazz.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        System.out.println(repoClazz.getName());
        return (Class<DefaultRepository<?>>) repoClazz;
    }
}


