package com.yj.bookstore.view;

import com.yj.bookstore.dao.BookDao;
import com.yj.bookstore.model.domain.Book;
import com.yj.bookstore.model.dto.PageObject;
import com.yj.bookstore.model.dto.SelectItem;
import lombok.Data;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 76355
 * @Date 2019/4/29 8:27
 * @Description
 */
@Named
@Data
public class LazyBookDataModel extends LazyDataModel<Book> {

    private List<Book> list;
    @Inject
    private BookDao bookDao;
    @Inject
    private SelectBean selectBean;

    public LazyBookDataModel(){ }

    @Override
    public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        //这个方法不光可以实现分页，也可以实现过滤与排序
        /*我们暂时只实现简单的分页，
           first代表起始位置，pageSize代表查询数量，其他参数可以暂时不管，这两个参数就可以实现分页功能
           要查询出数据与数量
        */
        List<SelectItem> selectItems=new ArrayList<>();
        Class classz=selectBean.getClass();
        Field[] fields=classz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field=fields[i];
            String name=field.getName();
            Method method=null;
            Object value=null;
            try {
                method=classz.getMethod("get"+name.substring(0,1).toUpperCase()
                        +name.substring(1), null);
                value=method.invoke(selectBean);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(value==null) continue;
            if(name.equals("bookName")){
                    selectItems.add(new SelectItem("book_name","like",value));
            }
            if(name.equals("startDate")){
                selectItems.add(new SelectItem("publish_date",">=",value));
            }
            if(name.equals("endDate")){
                selectItems.add(new SelectItem("publish_date","<=",value));
            }
            if(name.equals("category")){
                selectItems.add(new SelectItem("category","like",value));
            }
            if(name.equals("startPrice")){
                selectItems.add(new SelectItem("price",">=",value));
            }
            if(name.equals("endPrice")){
                selectItems.add(new SelectItem("price","<=",value));
            }
            if(name.equals("brief")){
                selectItems.add(new SelectItem("brief","like",value));
            }
        }
        PageObject<Book> pageObject=bookDao.findList(first,pageSize,selectItems);
        //setRowCount(数量); //这个方法一定要执行!
        setRowCount(pageObject.getTotal());
        //list = 查询结果
        list=pageObject.getList();
        return list;
    }

    @Override
    public Book getRowData(String rowKey)
    {
        for(Book book : list)
        {
            if(book.getId().toString().equals(rowKey))
            {
                return book;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Book book)
    {
        return book.getId();
    }

}
