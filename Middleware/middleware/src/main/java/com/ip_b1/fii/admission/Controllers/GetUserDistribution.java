package com.ip_b1.fii.admission.Controllers;

import com.ip_b1.fii.admission.DTO.AuthEntity;
import com.ip_b1.fii.admission.DTO.NotificationsOutEntity;
import com.ip_b1.fii.admission.ServerProperties;
import com.ip_b1.fii.admission.Utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by fenea on 5/7/2017.
 */

@RestController
@RequestMapping("/controller/get_distribution")
public class GetUserDistribution {


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> getDistribution(@RequestBody AuthEntity auth) {
        if (!AuthUtils.checkAuth(auth)) {
            return new ResponseEntity<String>(
                    new String(),
                    HttpStatus.UNAUTHORIZED
            );
        } else {
            RestTemplate template = new RestTemplate();

            ResponseEntity<String> entity = template.getForEntity(
                    ServerProperties.modelUrl + "get_distribution",
                    String.class
            );

            return new ResponseEntity<>(
                    entity.getBody(),
                    HttpStatus.OK
                    //
            );
        }
    }

}
