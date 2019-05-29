package com.wm.tests;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.wm.service.TicketService;

@Profile("test")
@Configuration
public class TicketServiceTestConfiguration {
   @Bean
   @Primary
   public TicketService productService() {
      return Mockito.mock(TicketService.class);
   }
}