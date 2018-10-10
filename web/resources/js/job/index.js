$(function () {
    var $table = $('#table');
    init();

    function init() {
        // select2初始化
        $('select').select2({
            minimumResultsForSearch: -1
        });
        initTable();
    }

    $(document).on('focus', 'input[type="text"]', function () {
        $(this).parent().find('label').addClass('active');
    }).on('blur', 'input[type="text"]', function () {
        if ($(this).val() == '') {
            $(this).parent().find('label').removeClass('active');
        }
    });
    var editor;

    /**
     * 初始化表格
     */
    function initTable(pid) {
        var url = API_PATH + '/job/query';

        $table.bootstrapTable({
            url: url,
            responseHandler: function (res) {
                return {
                    total: res.records,
                    rows: res.rows
                }
            },
            queryParams: function (params) {
                var querys = {
                    P_NO: params.pageNumber,
                    P_SIZE: params.pageSize,
                    P_ORDER: params.sortOrder,
                    P_SORT: params.sortName,
                    Q_SEARCH: params.searchText,
                    Q_KEY: 'job_name,description,target_bean,target_method,creator'
                };
                if (pid) {
                    querys = {
                        P_NO: params.pageNumber,
                        P_SIZE: params.pageSize,
                        P_ORDER: params.sortOrder,
                        P_SORT: params.sortName,
                        Q_SEARCH: params.searchText,
                        Q_KEY: 'job_name,description,target_bean,target_method,creator',
                        Q_COLUMN: 'AND_parent_id_EQ,OR_id_EQ',
                        Q_VALUE: pid + ',' + pid
                    }
                }
                return querys;
            },
            pageSize: 8,
            pageNumber: 1,
            queryParamsType: '',
            height: getHeight(),
            pageList: [5, 10, 25, 50, 100],
            striped: true,
            search: true,
            searchOnEnterKey: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            minimumCountColumns: 2,
            showPaginationSwitch: true,
            clickToSelect: true,
            detailView: false,
            detailFormatter: 'detailFormatter',
            pagination: true,
            paginationLoop: false,
            classes: 'table table-hover table-no-bordered',
            sidePagination: 'server',
            silentSort: false,
            smartDisplay: false,
            idField: 'id',
            sortName: 'id',
            sortOrder: 'asc',
            escape: true,
            maintainSelected: true,
            toolbar: '#toolbar',
            columns: [
                {field: 'state', checkbox: true},
                {field: 'id', title: '编号', sortable: true, halign: 'center'},
                {field: 'name', title: '任务名称', sortable: true, halign: 'center'},
                {field: 'description', title: '描述', sortable: true, halign: 'center'},
                {field: 'cron', title: 'cron', sortable: true, halign: 'center'},
                {field: 'targetBean', title: '任务指定Bean', sortable: true, halign: 'center'},
                {field: 'targetMethod', title: '任务指定方法', sortable: true, halign: 'center'},
                {field: 'startTime', title: '开始时间', sortable: true, halign: 'center'},
                {field: 'endTime', title: '结束时间', sortable: true, halign: 'center'},
                {field: 'priority', title: '优先级', sortable: true, halign: 'center'},
                {
                    field: 'status', title: '状态', sortable: true, halign: 'center',
                    formatter: function (val, row, index) {
                        debugger
                        if (val == 0) {
                            return '<img src="/resources/images/stop.png" height="30" width="30"/>';
                        } else if (val == 1) {
                            return '<img src="/resources/images/running.png" height="30" width="30"/>';
                        }
                    }
                },
                {field: 'creator', title: '创建人', sortable: true, halign: 'center'},
                {field: 'createTime', title: '创建时间', sortable: true, halign: 'center',
                formatter:function (val, row, index) {
                    return dateFormatter(val);
                }},
                {field: 'updateTime', title: '修改时间', sortable: true, halign: 'center',
                    formatter:function (val, row, index) {
                        return dateFormatter(val);
                    }},
                {
                    field: 'action',
                    title: '操作',
                    halign: 'center',
                    align: 'center',
                    formatter: 'actionFormatter',
                    events: 'actionEvents',
                    clickToSelect: false
                }
            ]
        }).on('all.bs.table', function (e, name, args) {
            $('[data-toggle="tooltip"]').tooltip();
            $('[data-toggle="popover"]').popover();
        });
    }


    $('#add-save-btn').click(function () {
        var json = $('#add-form').serializeObject();
        if (!json.name) {
            $.message({
                type: 'warning',
                message: '请输入任务名称'
            })
            return false;
        }
        if (!json.cron) {
            $.message({
                type: 'warning',
                message: '请输入cron表达式'
            })
            return false;
        }
        if (!json.targetBean) {
            $.message({
                type: 'warning',
                message: '请输入任务指定Bean'
            })
            return false;
        }
        if (!json.targetMethod) {
            $.message({
                type: 'warning',
                message: '请输入任务指定方法'
            })
            return false;
        }
        $.ajax({
            url: API_PATH + '/job/save',
            type: 'post',
            data: json,
            success: function (res) {
                if (res.code == 200) {
                    $.message('添加成功');
                    $table.bootstrapTable('refresh');
                    if (!$('#continue-add-chk').is(':checked')){
                        $('#createDialog').modal('hide');
                    }
                } else {
                    $.message({
                        type: 'error',
                        message: res.msg
                    })
                }
            }
        })
    })

    window.actionEvents = {
        'click .resume': function (e, value, row, index) {
            if (row.status==1){
                $.message({
                    type:'warning',
                    message:'JOB已在运行中...'
                })
                return
            }
            $.ajax({
                url:API_PATH+'/job/operation/resume',
                type:'post',
                data:{
                    id:row.id,
                    targetBean:row.targetBean
                },
                success:function (res) {
                    if (res.code==200){
                        $.message(res.msg);
                        $table.bootstrapTable('refresh');
                    }else{
                        $.message({
                            type:'error',
                            message:res.msg
                        })
                    }
                }
            })
        },
        'click .edit': function (e, value, row, index) {
            $('#createDialog .modal-body label').attr('class', 'active');
            $('.modal-title').html('编辑JOB');
            $('#add-id').val(row.id);
            $('#add-name').val(row.name);
            $('#add-description').val(row.job);
            $('#add-cron').val(row.cron);
            $('#add-bean').val(row.targetBean);
            $('#add-method').val(row.targetMethod);
            $('#add-start-time').val(row.startTime);
            $('#add-end-time').val(row.endTime);
            $('#add-priority').val(row.priority);
            $('#add-status').val(row.status);
            $('select').select2({
                minimumResultsForSearch: -1
            });

            $('#createDialog').modal('show');
        },
        'click .pause': function (e, value, row, index) {
            if (row.status==0){
                $.message({
                    type:'warning',
                    message:'JOB已暂停...'
                })
                return
            }
            $.ajax({
                url:API_PATH+'/job/operation/pause',
                type:'post',
                data:{
                    id:row.id,
                    targetBean:row.targetBean
                },
                success:function (res) {
                    if (res.code==200){
                        $.message(res.msg);
                        $table.bootstrapTable('refresh');
                    }else{
                        $.message({
                            type:'error',
                            message:res.msg
                        })
                    }
                }
            })
        }
    };


    $('#add-btn').click(function () {
        $('.modal-title').html('新增服集群');
        document.getElementById("add-form").reset();
        $('#add-shell').show();
        $('#add-id').val('');
        $('#createDialog').modal('show');
    })

    $('#delete-batch-btn').click(function () {
        var rows = $table.bootstrapTable('getSelections');
        if (rows.length == 0) {
            $.confirm({
                title: false,
                content: '请至少选择一条记录！',
                autoClose: 'cancel|3000',
                backgroundDismiss: true,
                buttons: {
                    cancel: {
                        text: '取消',
                        btnClass: 'waves-effect waves-button'
                    }
                }
            });
        } else {
            $.confirm({
                type: 'red',
                animationSpeed: 300,
                title: false,
                content: '确认删除该服务吗？',
                buttons: {
                    confirm: {
                        text: '确认',
                        btnClass: 'waves-effect waves-button',
                        action: function () {
                            debugger;
                            var ids = [];
                            for (var i in rows) {
                                ids.push(rows[i].id);
                            }
                            $.ajax({
                                contentType: "application/json",
                                url: API_PATH + '/job/deleteBatchIds',
                                type: 'post',
                                //traditional: true,
                                data: JSON.stringify(ids),
                                success: function (res) {
                                    if (res.code == 200) {
                                        $.message('操作成功');
                                        $table.bootstrapTable('refresh');
                                    } else {
                                        $.message({
                                            type: 'error',
                                            message: res.msg
                                        })
                                    }
                                }
                            })
                        }
                    },
                    cancel: {
                        text: '取消',
                        btnClass: 'waves-effect waves-button'
                    }
                }
            });
        }
    })
});

/**
 * 时间格式化
 */
function dateFormatter(cellval) {
    var dateVal = cellval + "";
    if (cellval != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

        return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
    }
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function actionFormatter(value, row, index) {
    return [
        '<a class="edit ml10" href="javascript:void(0)" data-toggle="tooltip" title="编辑"><i class="glyphicon glyphicon-edit"></i></a>　',
        '<a class="resume ml10" href="javascript:void(0)" data-toggle="tooltip" title="恢复"><i class="glyphicon glyphicon-play"></i></a>　',
        '<a class="pause ml10" href="javascript:void(0)" data-toggle="tooltip" title="暂停"><i class="glyphicon glyphicon-pause"></i></a>　'
    ].join('');
}


function detailFormatter(index, row) {
    var html = [];
    $.each(row, function (key, value) {
        html.push('<p><b>' + key + ':</b> ' + value + '</p>');
    });
    return html.join('');
}
