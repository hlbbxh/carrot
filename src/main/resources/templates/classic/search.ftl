<#include "/classic/inc/layout.ftl"/>

<@layout "搜索:" + kw>

<div class="row streams"> <#--  这是检索返回的ftl页面  其内容回被 加载到  layout.ftl 中 container div 中  -->
    <div class="col-xs-12 col-md-9 side-left">
        <div class="posts ">
            <ul class="posts-list">
                <li class="content">
                    <div class="content-box posts-aside">
                        <div class="posts-item">搜索: ${kw} 共 ${results.totalElements} 个结果</div>
                    </div>
                </li>
                <#include "/classic/inc/posts_item.ftl"/> <#-- 这里是循环出来查询的文章 -->
                <#list results.content as row>
                    <@posts_item row false/>
                </#list>
                <#if !results?? || results.content?size == 0>
                    <li class="content">
                        <div class="content-box posts-aside">
                            <div class="posts-item">该目录下还没有内容!</div>
                        </div>
                    </li>
                </#if>
            </ul>
        </div>
        <div class="text-center"> <#-- 这是分页的工具  上一页 下一页  -->
            <@utils.pager request.requestURI, results, 5/>
        </div>
    </div>
    <#-- 侧边栏 -->
    <div class="col-xs-12 col-md-3 side-right">
        <#include "/classic/inc/right.ftl" />
    </div>
</div>
</@layout>

