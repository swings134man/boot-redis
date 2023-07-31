package com.boot.redis.about_sync.model;

import com.boot.redis.about_sync.domain.SyncObject;
import com.boot.redis.about_sync.domain.SyncObjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncService {

    private final SyncJpaRepository repository;
    private final ModelMapper modelMapper;

    public void save(SyncObjectDTO dto) {
        SyncObject entity = modelMapper.map(dto, SyncObject.class);

        repository.save(entity);

        log.info("entity = {}", entity);
    }

    public SyncObjectDTO findByName(String name) {
        SyncObject entity = repository.findByName(name);

        SyncObjectDTO resultDto = modelMapper.map(entity, SyncObjectDTO.class);

        return resultDto;
    }

}
