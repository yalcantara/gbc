package com.gbc.config.ws.controllers

import com.gbc.config.ws.entities.App
import com.gbc.config.ws.services.AppService
import org.springframework.context.annotation.Lazy
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.inject.Inject

@Lazy
@Controller
@RequestMapping("/apps")
class AppController {


    @Inject
    lateinit var srv: AppService


    @GetMapping
    fun list():ResponseEntity<List<App>>{
        val ans = srv.list();
        return ResponseEntity.ok(ans)
    }
}