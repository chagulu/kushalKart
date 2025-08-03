package com.kushalkart.service;

import com.kushalkart.admin.entity.Worker;
import com.kushalkart.admin.entity.WorkerAddress;
import com.kushalkart.dto.WorkerListingResponse;
import com.kushalkart.entity.UserAddress;
import com.kushalkart.repository.UserAddressRepository;
import com.kushalkart.admin.repository.WorkerRepository;
import com.kushalkart.admin.repository.WorkerAddressRepository;
import com.kushalkart.repository.ServiceDetailsRepository;
import com.kushalkart.entity.ServiceDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerAddressRepository workerAddressRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private ServiceDetailsRepository serviceDetailsRepository;

    public Page<WorkerListingResponse> getWorkers(Long userId, Long serviceCategoryId, int page, int size) {
        Optional<UserAddress> userAddressOpt = userAddressRepository.findByUserId(userId);
        if (userAddressOpt.isEmpty()) {
            return Page.empty();
        }

        String userPincode = userAddressOpt.get().getPincode();
        List<WorkerAddress> matchingAddresses = workerAddressRepository.findByPincode(userPincode);

        List<Long> workerIdsInPincode = matchingAddresses.stream()
                .map(addr -> addr.getWorker().getId())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (workerIdsInPincode.isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Worker> workerPage = workerRepository
                .findByIdInAndServiceCategoryId(workerIdsInPincode, serviceCategoryId, pageable);

        List<WorkerListingResponse> responseList = workerPage.getContent().stream().map(worker -> {
            WorkerAddress wa = workerAddressRepository.findFirstByWorker_Id(worker.getId()).orElse(null);
            ServiceDetails sd = serviceDetailsRepository.findById(worker.getServiceCategoryId()).orElse(null);

            WorkerListingResponse resp = new WorkerListingResponse();
            resp.setWorkerId(worker.getId());
            resp.setName(worker.getName());
            resp.setEmail(worker.getEmail());
            resp.setMobileNo(worker.getMobile());
            resp.setServiceName(sd != null && sd.getServiceCategory() != null ? sd.getServiceCategory().getName() : null);
            resp.setServiceDescription(sd != null ? sd.getDescription() : null);

            if (wa != null) {
                resp.setAddress(wa.getAddressLine1());
                resp.setCity(wa.getCity());
                resp.setState(wa.getState());
                resp.setPincode(wa.getPincode());
            }

            return resp;
        }).collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, workerPage.getTotalElements());
    }
}
