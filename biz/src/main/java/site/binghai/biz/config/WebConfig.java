package site.binghai.biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.binghai.biz.inters.ManagerLoginInter;
import site.binghai.biz.inters.UserLoginInter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/manage/index").setViewName("manager");
        registry.addViewController("/mlogin").setViewName("manager");
        registry.addViewController("/user/index").setViewName("student");
        registry.addViewController("/slogin").setViewName("student");
    }

    @Bean
    public UserLoginInter userLoginInter() {
        return new UserLoginInter();
    }
    @Bean
    public ManagerLoginInter managerLoginInter() {
        return new ManagerLoginInter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInter()).addPathPatterns("/user/**");
        registry.addInterceptor(managerLoginInter()).addPathPatterns("/manage/**");
    }
}

