package com.ip_b1.fii.admission.Controllers;

import com.ip_b1.fii.admission.DTO.*;
import com.ip_b1.fii.admission.ServerProperties;
import com.ip_b1.fii.admission.Utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("controller/(sessionId)/application_review_accept")
public class ApplicationReject {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SuccessEntity> reject(@PathVariable String sessionId, AuthEntity user, String CNP, String rejectionMessage){

        if(!AuthUtils.checkAuthIsAdmin(user)){
            return new ResponseEntity<>(new SuccessEntity(false), HttpStatus.UNAUTHORIZED);
        }

        if(!process(sessionId, CNP, rejectionMessage))
            return new ResponseEntity<>(new SuccessEntity(false), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new SuccessEntity(true), HttpStatus.OK);

    }

    private static boolean process(String sessionId, String cnp, String rejectionMessage) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<CandidateOutEntity> candidate = restTemplate.getForEntity(
                ServerProperties.modelUrl + "model/(sessionId)/get_candidate?cnp={cnp}",
                CandidateOutEntity.class,
                sessionId,
                cnp
        );

        if(candidate.getStatusCode() == HttpStatus.NOT_FOUND){
            return false;
        }

        restTemplate.postForEntity(
                ServerProperties.modelUrl + "/model/(sessionId)/change_status",
                new FormStatusEntity(candidate.getBody().getEmail(), "rejected"),
                SuccessEntity.class,
                sessionId);

        ResponseEntity<SuccessEntity> success = restTemplate.postForEntity(
                ServerProperties.modelUrl + "/model/(sessionId)/add_notification",
                new NotificationEntity(candidate.getBody().getEmail(), false, rejectionMessage),
                SuccessEntity.class,
                sessionId);
        return success.getBody().isSuccess();
    }

}
