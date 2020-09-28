#### **work1**

```mysql
-- 连续值问题 ROW_NUMBER给数据编号
-- 本题用(夺冠年份-数据编号)得到的值last_year进行判断
-- last_year相等则说明是连续的年份,e.g.(1991-1,1992-2,1993-3)

with tmp as(
  SELECT team, year, (year - row_number() OVER(PARTITION BY team ORDER BY year)) last_year 
  FROM t1
)
select team, count(last_year) wins
 from tmp
 group by team, last_year
 having wins >= 3;
```

#### **work2**

```mysql
-- 波峰波谷(三条数据中最大最小) 序列函数问题 
-- lag返回当前数据(DATA A)的上一行(DATA B) lead返回当前数据的下一行(DATA C)
-- A<B && A<C A波谷 A>B && A>c A波峰 
with tmp as(
select id, time, price,
 nvl(lag(price) over(partition by id order by time),0) as lag_price,
 nvl(lead(price) over(partition by id order by time),0) as lead_price
 from t2
)
select id, time, price, feature 
from (select id, time, price,
 case when price > lag_price and price > lead_price then 'Crest'
      when price < lag_price and price < lead_price then 'Trough'
      end feature
 from tmp
) tmp2 
where feature is not null;
```

#### **work3**.1

```mysql
-- first_value,last_value
-- string >> timestamp unix_timestamp
with tmp as(
select id, dt,
 first_value(dt) over(partition by id order by dt) as first_time,
 last_value(dt) over(partition by id order by dt) as last_time
 from t3
)
select id, count(1) stride, round(max(unix_timestamp(last_time,'yyyy/mm/dd hh:mm')-unix_timestamp(first_time,'yyyy/mm/dd hh:mm'))/60,2) duration
from tmp
group by id order by duration;
```

#### **work3.2**

```mysql
-- 第一次访问时间first_value与之后的每次放访进行求差(第一行下移) > 30则说明是两个不同的浏览时间
-- 找到最近一次三十分钟之内访问的时间 last_value 
-- partition by 
```

