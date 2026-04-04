package com.example.serviceimpl;

import com.example.dto.request.UserDetailsSearchRequest;
import com.example.model.UserDetails;
import com.example.repository.UserDetailsRepository;
import com.example.service.UserDetailsSearchService;
import com.example.specification.UserDetailsSpecification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsSearchServiceImpl implements UserDetailsSearchService {

    private final UserDetailsRepository repository;

    @Override
    public Page<UserDetails> search(UserDetailsSearchRequest request) {

        Sort sort = request.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        return repository.findAll(
                UserDetailsSpecification.filter(
                        request.getReligion(),
                        request.getCaste(),
                        request.getCity(),
                        request.getEducation()
                ),
                pageable
        );
    }
}