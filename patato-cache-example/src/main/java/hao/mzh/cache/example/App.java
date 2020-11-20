package hao.mzh.cache.example;

import halo.mzh.cache.starter.caffeine.annotation.CaffeineEvict;
import halo.mzh.cache.starter.caffeine.annotation.CaffeineGet;
import halo.mzh.cache.starter.caffeine.annotation.CaffeinePut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RequestMapping("/demo")
@RestController
public class App {

    @GetMapping("get")
    @CaffeineGet(name = "#name", nameSpace = "app")
    public String get(@RequestParam String name, @RequestParam Integer age) {
        System.out.println("=========获取进入=========");
        return name + age;
    }

    @GetMapping("evict")
    @CaffeineEvict(names = {"#name"}, nameSpace = "app")
    public String evict(@RequestParam String name, @RequestParam Integer age) {
        System.out.println("=========驱逐进入=========");
        return name + age;
    }

    @GetMapping("put")
    @CaffeinePut(name = "#name", nameSpace = "app")
    public String put(@RequestParam String name, @RequestParam Integer age) {
        System.out.println("=========添加进入=========");
        return name + age;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
