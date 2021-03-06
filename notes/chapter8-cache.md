## 分布式缓存的原理及应用
缓存分进程级缓存和分布式缓存，进程级缓存指将数据缓存在服务内部，通过Map，List
等结构实现存储；分布式缓存指将缓存数据单独放在分布式系统中，以便于缓存的统一
管理和存取

#### Redis
支持多种类型的数据结构，如String(字符串)、Hash(散列)、List(列表)、Set(集合)、
ZSet(有序集合)、Bitmap(位图)、HyperLogLog(超级日志)和Geospatial(地理空间)

### 分布式缓存设计的核心问题
分布式缓存的核心问题是以哪种方式进行缓存预热和缓存更新，以及如何优雅解决
缓存雪崩、缓存穿透、缓存降级等问题。

- 缓存预热：指用户请求数据前先将数据加载到缓存系统中。一般有系统启动加载、
  定时加载等方式
- 缓存更新：指在数据发生变化后及时将变化后的数据更新到缓存中
    - 定时更新：适合需要缓存的数据量不是很大的情况
    - 过期更新
    - 写请求更新：适用于用户对缓存数据和数据库数据有实时强一致性要求
    - 读请求更新：在用户读请求时，先判断该请求数据的缓存是否存在或已过期，
      如不存在或已过期，则进行底层数据库查询并重新缓存
- 缓存淘汰策略
    - FIFO：判断被存储的时间，里目前最远的数据优先被淘汰
    - LRU(Least Recently Used，最近最少使用)：判断缓存最近被使用的时间，
      距离当前时间最远的数据优先被淘汰
    - LFU(Least Frequently Used，最不经常使用)：在一段时间内，被使用
      次数最少的缓存优先被淘汰
- 缓存雪崩：指在同一时刻由于大量缓存失效，倒是大量原本应该访问缓存的请求都去
  查询数据库，而对数据库的CPU和内存造成巨大压力，严重的话会导致数据库宕机，
  从而形成一系列的连锁反应，使整个系统崩溃。一般有三种处理方法
    - 请求加锁：对于并发量并不是很多的应用，使用请求加锁排队的法案防止过多
      请求数据库
    - 失效更新：为每一个缓存数据都增加过期标记来记录缓存数据是否失效，如果
      缓存标记是小，则更新数据缓存
    - 设置不同的失效时间：为不同的数据设置不同的缓存失效时间，放置在同一
      时刻有大量缓存数据失效
- 缓存穿透：指由于缓存系统故障或用户频繁查询系统中不存在(在系统中不存在，意味着
  数据库和缓存中都不存在)的数据，而这时请求穿过缓存不断被发送到数据库，导致
  数据库过载，进而引发一连串并发问题。
    - 布隆过滤器：指将所有可能存在的数据都映射到一个足够大的Bitmap中，在
      用户发起请求先经过布隆过滤器拦截
    - cache null策略：指如果一个查询返回结果为null，我们仍然缓存这个null
      结果，但他的过期时间会很短，通常不超过5分钟
- 缓存降级：指由于访问量剧增导致服务器出现问题时，优先保障核心业务的运行，
  减少或关闭非核心业务对资源的使用
    - 写降级：在写请求增大时，可以只进行Cache的更新，然后将数据异步更新
      到数据库，保持最终一致性即可
    - 读降级：在数据库服务负载过高或数据库系统故障时，可以只对Cache进行读取，
      并将结果返回给用户。这种方式适用于对数据实时性要求不高的场景，保障了在
      系统发生故障的情况下用户依然能够访问到诗句
