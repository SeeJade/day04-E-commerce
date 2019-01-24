package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService extends BaseService<TbBrand> {
    /**
     * 获取品牌列表
     * @return 品牌列表
     */
    List<TbBrand> queryAll();

    /**
     * 根据分页信息分页查询品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    List<TbBrand> testPage(Integer page, Integer rows);

    /**
     * 根据条件模糊分页查询
     * @param page 页号
     * @param rows 页大小
     * @param brand 查询条件对象
     * @return 分页结果
     */
    PageResult search(Integer page, Integer rows, TbBrand brand);

    List<Map<String,String>> selectOptionList();

}
