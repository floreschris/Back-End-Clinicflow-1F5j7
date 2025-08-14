package com.flowired.service.impl;

import com.flowired.model.MediaFile;
import com.flowired.repo.IGenericRepo;
import com.flowired.repo.IMediaFileRepo;
import com.flowired.service.IMediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl extends CRUDImpl<MediaFile, Integer> implements IMediaFileService {

    private final IMediaFileRepo repo;

    @Override
    protected IGenericRepo<MediaFile, Integer> getRepo() {
        return repo;
    }
}
