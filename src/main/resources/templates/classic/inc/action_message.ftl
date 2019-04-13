<#if message??>
	<!-- 这是请求发出后返回的数据 -->
	<!-- 这是请求发出后返回的数据 -->
	<!-- 这是请求发出后返回的数据 -->
	<div class="alert alert-danger">
		<#--<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span></button>-->
		${message}
	</div>
</#if>
<#if data??>
	<#if (data.code >= 0)>
		<div class="alert alert-success">
			<#--<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span></button>-->
			${data.message}
		</div>
	<#else>
		<div class="alert alert-danger">
			<#--<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span></button>-->
			${data.message}
		</div>
	</#if>
</#if>