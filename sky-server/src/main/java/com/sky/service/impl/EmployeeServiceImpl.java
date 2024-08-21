package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /*
    * 新增员工*/
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程的id:"+Thread.currentThread().getId());

        //要先做一步对象转换，把前端传过来的DTO对象转化为实体类来交给持久层
        Employee employee=new Employee();

        //如果一个一个用set和get会非常麻烦，所以使用对象属性拷贝法
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置账号状态，默认正常
        employee.setStatus(StatusConstant.ENABLE);//使用常量类，避免硬编码

        //设置密码,默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //已做好公共字段自动填充
        /*//设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人id和修改人id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());*/

        employeeMapper.insert(employee);



    }

    /*
    * 分页查询*/
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);

        long total=page.getTotal();
        List<Employee> records=page.getResult();

        return new PageResult(total,records);
    }


    /*启用禁用员工账号*/
    public void startOrStop(Integer status,Long id){
        //执行修改/更新的sql语句
        //建议写成动态的，这样复用性强一些

       /* Employee employee=new Employee();
        employee.setStatus(status);
        employee.setId(id);*/

        Employee employee=Employee.builder()
                .status(status)
                .id(id)
                .build();


        employeeMapper.update(employee);
    }


    /*根据id查询员工*/
    public Employee getById(Long id){
        Employee employee=employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /*编辑员工信息*/
    public void update(EmployeeDTO employeeDTO){
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);

    }

}
