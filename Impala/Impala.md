#### **work1**

```sql
-- 按照用户ID分组 按照点击时间默认排序 
-- 表下移获得next_time计算与click_time的时间差 大于标1 小于标0
-- 到这一步已经可以获得间隔大于30min的点击行为,(00010001)该如何进行分区？
-- 通过累加A 0 0 0 1 1 B 0 0 1 
with tmp2 as(
select user_id,click_time,
  sum(mark) over(partition by user_id order by click_time rows between unbounded preceding and current row) mark
  from (select user_id,click_time,
  time_diff,if(time_diff>=30,1,0) mark
  from (select user_id,click_time,nvl((unix_timestamp(click_time)- unix_timestamp(lag(click_time) over(partition by user_id order by click_time)))/60,0) time_diff 
  from user_clicklog) tmp1
 ) tmp
)

select user_id,click_time,row_number() over(partition by mark,user_id order by click_time) rank 
  from tmp2;
```






