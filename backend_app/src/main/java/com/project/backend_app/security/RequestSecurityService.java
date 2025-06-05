package com.project.backend_app.security;

import com.project.backend_app.dto.request.RequestDto;
import com.project.backend_app.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service providing custom security checks related to Requests.
 * This bean is referenced in @PreAuthorize expressions.
 */
@Service("requestSecurityService") // The name "requestSecurityService" must match the one used in SpEL
public class RequestSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(RequestSecurityService.class);
    private final RequestService requestService;

    @Autowired
    public RequestSecurityService(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Checks if the currently authenticated user is the owner of the specified request.
     * The owner is determined by comparing the authenticated user's username
     * with the username of the customer associated with the request.
     *
     * @param authentication The current user's authentication object.
     * @param requestId The ID of the request to check.
     * @return True if the authenticated user is the owner of the request, false otherwise.
     */
    public boolean isOwnerOfRequest(Authentication authentication, Long requestId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("isOwnerOfRequest check called with unauthenticated user for requestId: {}", requestId);
            return false;
        }

        String currentUsername = authentication.getName();
        if (currentUsername == null) {
            logger.warn("isOwnerOfRequest check called, but current username is null for requestId: {}", requestId);
            return false;
        }

        try {
            RequestDto requestDto = requestService.getRequestById(requestId);

            if (requestDto.getCustomerUsername() == null) {
                logger.warn("Request {} does not have an associated customer username.", requestId);
                return false;
            }

            boolean isOwner = currentUsername.equals(requestDto.getCustomerUsername());
            if (!isOwner) {
                logger.debug("Ownership check failed for requestId: {}. Authenticated user: {}, Request owner: {}",
                        requestId, currentUsername, requestDto.getCustomerUsername());
            }
            return isOwner;

        } catch (RuntimeException e) {
            logger.warn("Exception during ownership check for requestId: {}. User: {}. Exception: {}",
                    requestId, currentUsername, e.getMessage());
            return false;
        }
    }
}