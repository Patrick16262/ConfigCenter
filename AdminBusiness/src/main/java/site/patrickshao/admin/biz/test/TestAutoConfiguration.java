package site.patrickshao.admin.biz.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */

@EnableAutoConfiguration
public class TestAutoConfiguration {

//    @Bean
//    public List<BaseMapper<AccessTokenPO>> repo(SqlSessionFactory sqlSessionFactory) throws Exception {
//
//        Class<?> mapperClass = new ByteBuddy()
//                .makeInterface(TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, AccessTokenPO.class).build())
//                .name("repo")
//                .annotateType(AnnotationDescription.Builder.ofType(Mapper.class).build())
//                .make()
//                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
//                .getLoaded();
//        MapperFactoryBean<?> factoryBean = new MapperFactoryBean<>(mapperClass);
//        factoryBean.setSqlSessionFactory(sqlSessionFactory);
//        sqlSessionFactory.getConfiguration().addMapper(mapperClass);
//        return (BaseMapper<AccessTokenPO>) factoryBean.getObject();
//
//    }
}
