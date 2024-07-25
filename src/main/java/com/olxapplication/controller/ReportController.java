package com.olxapplication.controller;

import com.olxapplication.config.RabbitMQSender;
import com.olxapplication.dtos.NotificationRequestDto;
import com.olxapplication.dtos.ResponseMessageDto;
import com.olxapplication.dtos.UserMailDTO;
import com.olxapplication.entity.User;
import com.olxapplication.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

/**
 * This controller class provides API endpoints for generating reports within the application.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/report")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class ReportController {
    private static final String URL = "https://deployedmicroservice.onrender.com/microservice/receiver";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ReportService csvReportService;
    @Autowired
    private final RabbitMQSender rabbitMQSender;
    @Autowired
    private ReportService reportService;

    /**
     * Generate a report based on the chosen file format.
     * @param strategy The format of the report file.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/announcement/get".
     * */
    @GetMapping("/generate")
    public ModelAndView generateReport(@ModelAttribute("strategy") String strategy, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/announcement/get");
        String msg = reportService.generateReport(strategy);

        String pathName = "C:\\Users\\prico\\OneDrive\\Desktop\\Faculta\\PS\\A2\\";
        if(strategy.equals("csv")){
            pathName += "Reports\\CSV_Report.csv";
        }
        if(strategy.equals("pdf")){
            pathName += "Reports\\PDF_Report.pdf";
        }
        if(strategy.equals("txt")){
            pathName += "Reports\\TXT_Report.txt";
        }

        User admin = reportService.getAdmin();

        // Crearea unui nou UserDto
        UserMailDTO userMailDTO = new UserMailDTO(admin.getId()
                , admin.getFirstName()
                , admin.getLastName()
                , admin.getEmail()
                , "report"
                , pathName);

        // Crearea HttpHeaders și setarea token-ului
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(userMailDTO.getId() + userMailDTO.getEmail()); // presupunem că token-ul este disponibil

        // Crearea NotificationRequestDto și HttpEntity
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(userMailDTO.getId(), userMailDTO.getFirstName() + " " + userMailDTO.getLastName(), userMailDTO.getEmail(), userMailDTO.getAction(), userMailDTO.getFilePath()); // completați cu datele necesare
        HttpEntity<NotificationRequestDto> entity = new HttpEntity<>(notificationRequestDto, headers);

        // Apelarea metodei restTemplate.exchange
        ResponseMessageDto response = restTemplate.exchange(URL, HttpMethod.POST, entity, ResponseMessageDto.class).getBody();
        //System.out.println("!!!!!!!!------------>" + response + "<------------!!!!!!!!");

        redirectAttributes.addFlashAttribute("message", msg);
        return modelAndView;
    }
}
