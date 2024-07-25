package com.example.Project2.controller;

import com.example.Project2.bean.Archive;
import com.example.Project2.dao.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class archiveController {
    @Autowired
    private ArchiveRepository archiveRepository;
    /**
     *查询所有列表
     */
    @GetMapping(value = "/archives")
    public List<Archive> findlist(){
        return archiveRepository.findAll();
    }
    /**
     *code查询一条数据
     */
    @GetMapping(value = "/select") // http://localhost:8080/select?code={code}
    public Archive findcode(@RequestParam(value = "code",defaultValue = "1") int code){

        return archiveRepository.findById(code).orElse(null);
    }
    /**
     *name 查询
     */
    @GetMapping(value = "/archives/name/{name}")
    public List<Archive> findlist1(@PathVariable("name") String name){
        return archiveRepository.findByName(name);
    }
    /**
     *添加一条数据
     */
    @PostMapping(value = "/archives")
    public Archive add(@RequestBody Archive archive) {
        return archiveRepository.save(archive);
    }
    /**
     *更新
     */
    @PutMapping(value= "/archives/{code}")
    public Archive update(@PathVariable("code") Integer code,
                       @RequestParam(value = "name") String name){
        Archive archive = new Archive();
        archive.setCode(code);
        archive.setName(name);
        System.out.println(code+name);
        return archiveRepository.save(archive);
    }
    /**
     *更新
     */
    @DeleteMapping(value= "/archives/{code}")
    public void delete(@PathVariable("code") Integer code){

        archiveRepository.delete(Objects.requireNonNull(archiveRepository.findById(code).orElse(null)));
    }
}
