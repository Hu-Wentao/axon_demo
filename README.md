# Axon3.4 使用教程

## 0 引入
添加 build.gradle.kts依赖
```gradle.kts
implementation("org.axonframework:axon-spring-boot-autoconfigure:3.4.3")
```

## 1 Command
命令
一般使用 data class,
命名 `XxxCmd`
对于内部持有的 聚合根id属性, 需要添加 @TargetAggregateIdentifier 注解, 以确保框架能够查询到对应的聚合根实例

## 2 Event
事件
一般使用 data class
命名 `XxxEvt`

## 3 Aggregate 聚合根
如果使用Spring,则需要添加 `@Aggregate`

一般使用 class
命名无要求


> 关于聚合根中, @CommandHandler与@EventSourcingHandler注解,参见
> axon介绍 https://docs.axoniq.io/reference-guide/v/3.4/part-ii-domain-logic/command-model

### 3.1 字段添加 @AggregateIdentifier
为聚合根ID添加注解,
注意: 如果聚合根使用了JPA的 @Id 注解, 则无需再次添加本注解

### 3.2 聚合根添加无参构造
聚合根作为一个class, 默认有一个无参构造,但如果添加了一个以`XxxCmd`作为参数的构造,默认就不会添加无参构造
因此必须先声明一个无参构造
constructor()
无参构造被用于 Axon框架的`事件溯源` 和 JPA中(如果有的话),

###3.2b 基于Axon4
使用 `@CreationPolicy` 注解, 替代构造函数, 这样无需手动创建`constructor()`

### 3.3 方法 添加Cmd构造
注意为构造添加 `@CommandHandler` 注解, 表示用于处理XxxCmd


### 3.4 方法体 添加AggregateLifecycle.`apply`
一般在 commandHandler方法内部调用, 用于发送Event 


### 3.5 方法 添加@EventSourcingHandler
处理源事件的方法, 

## 4 QueryModel 查询模型(投影 Projection) 
如果使用Spring,则需要添加 `@Component`

### 4.1 方法 添加@EventHandler
用于更新 QueryModel,当Evt被发布, 将事件添加到集合中, 更新Projection(查询模型) 
更多信息参见 https://docs.axoniq.io/reference-guide/v/3.4/part-ii-domain-logic/event-handling

### 4.2 方法 添加@QueryHandler
更新信息参见 https://docs.axoniq.io/reference-guide/v/3.4/part-ii-domain-logic/query-handling


## 5 配置 Configuration

如果使用了Spring框架, 则只需要为 聚合根添加 `@Aggregate`, 为Projection添加`@Component`
即完成自动配置.
单元测试/不使用Spring,则需要手动配置
详见 https://docs.axoniq.io/reference-guide/v/3.4/part-i-getting-started/quick-start#configuration
