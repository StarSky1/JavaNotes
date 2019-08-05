//resultSet转List<Bean>,bean中包含的类型可以为String\int\boolean\timestamp
    //注意：这里要求JavaBean存在一个空的构造函数
    public <T> ArrayList<T> resultToList(ResultSet resultSet, Class<T> clazz) {
        //创建一个 T 类型的数组
        ArrayList<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            //遍历resultSet
            while (resultSet.next()) {
                //通过反射获取对象的实例
                T t = clazz.getConstructor().newInstance();
                //遍历每一列
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    //获取列的名字
                    String fName = metaData.getColumnLabel(i + 1);

                    //因为列的名字和我们EMP中的属性名是一样的，所以通过列的名字获得其EMP中属性
                    //通过field可以得到class的字段名和字段值类型
                    Field field = clazz.getDeclaredField(fName);

                    //为避免字符串拼接，可以通过PropertyDescriptor反射得到fName字段的get、set方法
                    //通过getReadMethod()方法调用类的get函数
                    //通过getWriteMethod()方法来调用类的set方法
                    PropertyDescriptor descriptor = new PropertyDescriptor(fName, clazz);
                    Method setMethod = descriptor.getWriteMethod();
                    //这里需要注意，应该通过resultSet.getObject(i + 1, field.getType())来传递类型遍历得到resultSet里面的值
                    //直接通过resultSet.getObject(i+1)会报错
                    setMethod.invoke(t, resultSet.getObject(i + 1, field.getType()));
                }
                //把赋值后的对象 加入到list集合中
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info(e.getLocalizedMessage());
            LOGERROR.error(e.getLocalizedMessage());
        }
        // 返回list
        return list;
    }