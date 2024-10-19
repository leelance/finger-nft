package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * StorageService
 *
 * @author admin
 * @since 2024/10/19 12:22
 */
@Service
@RequiredArgsConstructor
public class FcStorageService {
  private final IBaseService baseService;

  public void deleteByKey(String key) {
    QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();
    wrapper.eq(FcStorage.KEY, key);
    this.baseService.deleteByCondition(FcStorage.class, wrapper);
  }

  public void add(FcStorage storageInfo) {
    this.baseService.save(storageInfo);
  }

  public FcStorage findByKey(String key) {
    QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();
    wrapper.eq(FcStorage.KEY, key);
    return this.baseService.getByCondition(FcStorage.class, wrapper);
  }

  public int update(FcStorage storageInfo) {
    return this.baseService.update(storageInfo);
  }

  public FcStorage findById(Long id) {
    return this.baseService.getById(FcStorage.class, id);
  }

  public IPage<FcStorage> querySelective(String key, String name, Integer page, Integer limit, String sort, String order) {
    QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();

    if (!StringUtils.isEmpty(key)) {
      wrapper.eq(FcStorage.KEY, key);
    }
    if (!StringUtils.isEmpty(name)) {
      wrapper.like(FcStorage.NAME, name);
    }

    wrapper.eq(BaseEntity.DELETED, false);

    Page<FcStorage> pageInfo = new Page<>(page, limit);

    if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
      OrderItem orderItem = new OrderItem(sort, "desc".equalsIgnoreCase(order));
      pageInfo.addOrder(orderItem);
    }

    return this.baseService.findByPage(FcStorage.class, wrapper, pageInfo);
  }

  public IPage<FcStorage> querySelective(String key, String name, Page<FcStorage> pageInfo, boolean asc, String sort) {
    QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();

    if (!StringUtils.isEmpty(key)) {
      wrapper.eq(FcStorage.KEY, key);
    }
    if (!StringUtils.isEmpty(name)) {
      wrapper.like(FcStorage.NAME, name);
    }

    wrapper.eq(BaseEntity.DELETED, false);
    if (!StringUtils.isEmpty(sort)) {
      OrderItem orderItem = new OrderItem(sort, asc);
      pageInfo.addOrder(orderItem);
    }
    return this.baseService.findByPage(FcStorage.class, wrapper, pageInfo);
  }
}
