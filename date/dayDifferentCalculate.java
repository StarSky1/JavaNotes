
/**
* 天数间隔计算器
*/
public static void main(String[] args) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(2019,9,16);
        Date now=calendar.getTime();
        System.out.println(calendar.getTime());
        calendar.set(2019,5,6);
        Date before=calendar.getTime();
        System.out.println(before);
        long diff=now.getTime()-before.getTime();
        long bottom=1000*60*60*24;
        System.out.println(diff/bottom);
}