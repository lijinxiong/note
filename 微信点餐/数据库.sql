-- 商品表
CREATE TABLE
             `product_info`
             (
                          `product_id`          VARCHAR(32) NOT NULL
                        , `product_name`        VARCHAR(64) NOT NULL COMMENT '商品名称'
                        , `product_price`       DECIMAL(8, 2) NOT NULL COMMENT '商品价格'
                        , `product_stock`       INT NOT NULL COMMENT '商品库存'
                        , `product_description` VARCHAR(64) COMMENT '描述'
                        , `product_icon`        VARCHAR(512) COMMENT '小图'
                        , `category_type`       INT NOT NULL COMMENT '类目编号'
                        , `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间'
                        , `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON
UPDATE
       current_timestamp COMMENT '修改时间'
     , `product_status` TINYINT(3) DEFAULT '0' COMMENT '商品状态,0正常1下架'
     , PRIMARY KEY (`product_id`)
             )
             COMMENT '商品表'
;

-- 类目表
CREATE TABLE
             `product_category`
             (
                          `category_id`   INT NOT NULL AUTO_INCREMENT
                        , `category_name` VARCHAR(64) NOT NULL COMMENT '类目名字'
                        , `category_type` INT NOT NULL COMMENT '类目编号'
                        , `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间'
                        , `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON
UPDATE
       current_timestamp COMMENT '修改时间'
     , PRIMARY KEY (`category_id`)
     , UNIQUE KEY `uqe_category_type`(`category_type`)
             )
             COMMENT '类目表'
;