package com.sparkplug.listings.infrastructure.security.aop;

import com.sparkplug.commonauthentication.user.SparkplugUserDetails;
import com.sparkplug.listings.domain.repository.ListingsRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckListingOwnershipAspect {

    private final ListingsRepository listingRepository;

    @Autowired
    public CheckListingOwnershipAspect(ListingsRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @Before(value = "@annotation(CheckListingOwnership) && args(id, ..)")
    public void checkOwnership(JoinPoint joinPoint, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = ((SparkplugUserDetails) authentication.getPrincipal()).getId();

        if (!listingRepository.existsByIdAndCreatorId(id, userId)) {
            throw new AuthorizationDeniedException("You do not own this resource.");
        }
    }
}
