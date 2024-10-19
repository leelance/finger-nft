package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FcStorageManager
 *
 * @author admin
 * @since 2024/10/19 12:30
 */
@Service
@RequiredArgsConstructor
public class FcStorageManager {
  private final IBaseService baseService;

  public void add(FcStorage storageInfo) {
    this.baseService.save(storageInfo);
  }

  /**
   * 批量保存storage
   *
   * @param list List<FcStorage>
   */
  public void batchSave(List<FcStorage> list) {
    baseService.saveBatch(list);
  }

  public FcStorage findByKey(String key) {
    QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();
    wrapper.eq(FcStorage.KEY, key);
    return this.baseService.getByCondition(FcStorage.class, wrapper);
  }

  public int update(FcStorage storageInfo) {
    return this.baseService.update(storageInfo);
  }

  public FcStorage get(Long id) {
    return this.baseService.getById(FcStorage.class, id);
  }

  public FcStorage findById(Long id) {
    return this.baseService.getById(FcStorage.class, id);
  }

}
