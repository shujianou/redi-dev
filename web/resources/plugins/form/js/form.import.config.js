/**
 * 表单导入配置
 */
(function($){
	
	/**
	 * 表单导入配置监听
	 */
	$.fn.formImportConfigListener = function(options) {
		var $this = $(this);
		var seqNum= 1;
        var rowsCount = options.rowsCount;
        var formId = options.formId;
		//添加字段
		$(".add-field").click(function(){
            var $tbody = $this.find("table tbody");
            _addTr($tbody);
		});
		if(utils.isEmpty(formId)) {
		    _init();
        }
        _selectTableListener();
        _clickOkBtnListener();

        function _selectTableListener() {
            //加载列表绑定表列表
            var tableDefValue = $('#import-table-id').data('default-value');
            utils.selectItem("#import-table-id",'form/table/item.json',tableDefValue,function(val) {
                _loadItemDatas(val);
            });
            $("#import-table-id").change(function(){
                _loadItemDatas($(this).val());
            });
        }

        /**
         * 加载字段列表
         */
        function _loadItemDatas(val) {
            var defValue = null;
            var datas = null;
            $.ajax({
                url:'form/table/fields.json?id='+val,
                type:'GET',
                async:false,
                dataType:'json',
                success:function(data){
                    var output = data;
                    if(output.result=='1') {
                        datas = output.datas;
                    }
                }
            });

            $("#form-import-config-wrap").find(".bind-field").each(function () {
                var $select = $(this);
                var defValue = $select.data('default-value');
                var id = $select.attr('id');
                utils.selectDataItem("#"+id, datas, defValue, null);
            });
        }

		//删除字段
		$(".close").click(function(){
			$(this).parent().parent().remove();
		});

        function _clickOkBtnListener() {
            $("#form-import-config-ok").click(function () {
                var $form = $(this).closest('form');
                var configParam = {
                    tableId: $('#import-table-id').val(),
                    importType: $('#import-import-type').val(),
                    separator: $("#import-separator").val(),
                    startRow: $('#import-start-row').val(),
                    fields:[]
                };
                $form.find(".config-field").each(function () {
                    var $tr = $(this);
                    configParam.fields.push({tableFieldId: $tr.find('.bind-field').val(),
                        columnNum: $tr.find('.bind-col').val()});
                });
                if(configParam.fields.length == 0) {
                    utils.showMsg("请配置字段与列直接的关联");
                    return false;
                }
                $("#import-config-args").val(JSON.stringify(configParam));
                var $btn = $("#form-import-config-btn");
                var uri = $btn.data('uri');
                uri = uri.replace(/formId=(.*)/, 'formId=');
                $btn.data('uri', uri);
                BootstrapDialogUtil.close();
            });
        }

        /**
         * 添加行
         * @param $tbody
         * @param fieldVal
         * @param colNum
         * @private
         */
        function _addTr($tbody, fieldVal, colNum) {
            if(!utils.isEmpty($tbody.html())) {
                var $lastTr = $tbody.find("tr:last");
                var lastSeqNum = $lastTr.find(".seq-num").text();
                seqNum = parseInt(lastSeqNum)+1;
            }
            var $tmpl = $("#form-import-config").find(".tr-tmpl").clone();
            $tmpl.removeClass("tr-tmpl hidden");
            $tmpl.addClass("config-field");
            $tmpl.find(".seq-num").text(seqNum);
            $tmpl.find(".del").attr("id", "del"+seqNum);
            var $bindField = $tmpl.find(".bind-field");
            $bindField.attr("id", "bind-field-"+seqNum);
            if(utils.isNotEmpty(fieldVal)) {
                $bindField.val(fieldVal);
                $bindField.data('default-value', fieldVal);
            }
            if(utils.isNotEmpty(colNum)) {
                $tmpl.find(".bind-col").val(colNum);
            }
            $tbody.append($tmpl);
            $("<button type=\"button\" title=\"删除\" class=\"close\" data-dismiss=\"tr1\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>").click(function(){
                $(this).parent().parent().remove();
            }).appendTo("#del"+seqNum);
        }
        
        function _init() {
            var configArgs = $('#import-config-args').val();
            if(utils.isNotEmpty(configArgs)) {
                configArgs = JSON.parse(configArgs);
                $('#import-table-id').val(configArgs.tableId).data('default-value', configArgs.tableId);
                $('#import-import-type').val(configArgs.importType);
                $("#import-separator").val(configArgs.separator);
                $('#import-start-row').val(configArgs.startRow);
                var $tbody = $("#form-import-config-wrap .form-import-config-field tbody");
                for(var i = 0; i < configArgs.fields.length; i++) {
                    _addTr($tbody, configArgs.fields[i].tableFieldId, configArgs.fields[i].columnNum);
                }

            }

        }
	}
	
})(jQuery);