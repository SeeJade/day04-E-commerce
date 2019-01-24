package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SpecificationService.class)
public class SpecificationServiceImpl extends BaseServiceImpl<TbSpecification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbSpecification specification) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(specification.getSpecName())){
            criteria.andLike("spaceName", "%" + specification.getSpecName() + "%");
        }

        List<TbSpecification> list = specificationMapper.selectByExample(example);
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addNew(Specification specification) {
        specificationMapper.insertSelective(specification.getSpecification());

        if(specification.getSpecificationOptionList()!=null && specification.getSpecificationOptionList().size()>0){
            for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()){
                specificationOption.setSpecId(specification.getSpecification().getId());
                specificationOptionMapper.insertSelective(specificationOption);
            }
        }
    }

    @Override
    public Specification findOne(Long id) {
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
        tbSpecificationOption.setSpecId(id);
        List<TbSpecificationOption> select = specificationOptionMapper.select(tbSpecificationOption);
        return new Specification(tbSpecification,select);
    }

    @Override
    public void updateNew(Specification specification) {
        TbSpecification specification1 = specification.getSpecification();
        specificationMapper.updateByPrimaryKeySelective(specification1);

        TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
        tbSpecificationOption.setSpecId(specification1.getId());
        specificationOptionMapper.delete(tbSpecificationOption);

        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
        if(specificationOptionList!=null && specificationOptionList.size()>0){
            for (TbSpecificationOption tbs : specificationOptionList){
                tbs.setSpecId(specification1.getId());
                specificationOptionMapper.insertSelective(tbs);
            }
        }
    }

    @Override
    public List<Map<String,String>> selectOptionList() {
        return specificationMapper.selectOptionList();
    }
}
