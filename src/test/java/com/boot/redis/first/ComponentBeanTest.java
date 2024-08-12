package com.boot.redis.first;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SpringBootTest
class ComponentBeanTest {

    @Autowired
    ComponentBeanOne componentBeanOne;

    @Autowired
    ExamComponent examComponent;

    @Autowired
    ExamConfiguration examConfiguration;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("1. Component Class = Singleton")
    void use_1() {
        // 해당 method는 ComponentBeanOne 객체를 반환하므로 같은 객체를 반환함.
        ComponentBeanOne aThis = componentBeanOne.getThis();
        ComponentBeanOne bThis = componentBeanOne.getThis();

        System.out.println("aThis = " + aThis);
        System.out.println("bThis = " + bThis);

        Assertions.assertThat(aThis).isEqualTo(bThis);
        Assertions.assertThat(aThis).isEqualTo(componentBeanOne);
    }

    @Test
    @DisplayName("2. Component 내부 Bean 메서드 호출시 매번 새로운 객체 생성")
    void test2() {
        // Component 내부 Bean 메서드 호출시 매번 새로운 객체 생성
        // exam() 메서드는 특정 객체를 반환 하는데, 매번 새로운 객체를 생성하여 반환함.
        Exam exam = examComponent.exam();
        Exam exam2 = examComponent.exam();

        Assertions.assertThat(exam).isNotEqualTo(exam2);

        Object exam2Bean = applicationContext.getBean("exam2"); // exam2 라는 이름으로 등록된 Bean 객체를 가져옴.
        System.out.println("exam2Bean = " + exam2Bean);
    }

    @Test
    @DisplayName("3. Configuration 내부 Bean 메서드 호출시 같은 객체 생성")
    void test3() {
        Exam exam = examConfiguration.exam();
        Exam exam2 = examConfiguration.exam();

        Assertions.assertThat(exam).isEqualTo(exam2);

        Object examConf = applicationContext.getBean("examConf");
        System.out.println("examConf = " + examConf);
    }

}

// Component - 기본적으로 Singleton으로 생성되어 관리된다.
/*
    @Configuration 은 클래스를 프록시 객체로 감싸서 관리.
    즉 @Bean 메서드가 호출될때 프록시가 Spring Container 에 등록된 Bean 을 반환하도록 함.(싱글톤 Bean 반환)
 */
@Component
class ComponentBeanOne {
    public ComponentBeanOne getThis() {
        return this;
    }
}

class Exam {
}

/*
    해당 클래스 자체는 Singletone 으로 생성되어 관리 되지만.
    내부의 Method 는 일반 메서드 처럼 동작됨. -> 즉 exam() 메서드를 호출할 때마다 새로운 객체를 생성하여 반환함.

    @Component 내부 @Bean 메서드는 CGLIB 프록시로 감싸지지 않기 때문.

    메서드 호출시 같은 객체를 반환하기를 원한다면 @Configuration 을 사용하여 @Bean 메서드를 정의해야 함. not @Component
 */
@Component
class ExamComponent {

    @Bean(name = "exam2")
    public Exam exam() {
        return new Exam();
    }
}

@Configuration
class ExamConfiguration {

    @Bean(name = "examConf")
    public Exam exam() {
        return new Exam();
    }
}