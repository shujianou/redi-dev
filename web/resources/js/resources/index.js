
$(function () {
    var $table = $('#table');
    var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        edit: {
            enable: false
        },
        callback:{
            onClick:zTreeOnClick
        }
    };


    var zNodes =[];

    init();

    function init() {
        initTree();
        initTable();
    }
    
    function zTreeOnClick(e, id, node) {
        $('.add-btn').attr('pid',node.id);
        $table.bootstrapTable('destroy');
        initTable(node.id);
    }


    /**
     * 初始化资源树
     */
    function initTree() {
        $.ajax({
            url: API_PATH+'/resource/menuNodeList',
            type: 'post',
            async: false,
            dataType: 'json',
            success: function (res) {
                zNodes=res.data;
            }
        });
        zNodes.push({
            id:0,pId:null,name:'根'
        });
        $.fn.zTree.init($("#resTree"), setting, zNodes);
    }
    var newCount = 1;
    function addHoverDom(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='add node' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_"+treeNode.tId);
        if (btn) btn.bind("click", function(){
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
            return false;
        });
    };
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    };

    $(document).on('focus', 'input[type="text"]', function () {
        $(this).parent().find('label').addClass('active');
    }).on('blur', 'input[type="text"]', function () {
        if ($(this).val() == '') {
            $(this).parent().find('label').removeClass('active');
        }
    });

    /**
     * 初始化表格
     */
    function initTable(pid) {
        var url= API_PATH + '/resource/query';

        $table.bootstrapTable({
            url: url,
            responseHandler: function (res) {
                return {
                    total: res.records,
                    rows: res.rows
                }
            },
            queryParams: function (params) {
                var querys={
                    P_NO: params.pageNumber,
                    P_SIZE: params.pageSize,
                    P_ORDER: params.sortOrder,
                    P_SORT: params.sortName,
                    Q_SEARCH: params.searchText,
                    Q_KEY:'name,configure,creator,reviser'
                };
                if (pid){
                    querys={
                        P_NO: params.pageNumber,
                        P_SIZE: params.pageSize,
                        P_ORDER: params.sortOrder,
                        P_SORT: params.sortName,
                        Q_SEARCH: params.searchText,
                        Q_KEY:'name,configure,creator,reviser',
                        Q_COLUMN:'AND_parent_id_EQ,OR_id_EQ',
                        Q_VALUE:pid+','+pid
                    }
                }
                return querys;
            },
            pageSize: 8,
            pageNumber: 1,
            queryParamsType:'',
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
            detailView: true,
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
                {field: 'name', title: '资源名称', sortable: true, halign: 'center'},
                {field: 'parentId', title: '父级编号', sortable: true, halign: 'center'},
                {field: 'configure', title: '配置', sortable: true, halign: 'center'},
                {field: 'type', title: '类型', sortable: true, halign: 'center',
                    formatter:function (val, row, index) {
                        if (val==1){
                            return '菜单';
                        }else if (val==2){
                            return '按钮';
                        }
                    }},
                {field: 'sort', title: '排序', sortable: true, halign: 'center'},
                {field: 'creator', title: '创建人', sortable: true, halign: 'center'},
                {field: 'reviser', title: '修改人', sortable: true, halign: 'center'},
                {field: 'createTime', title: '创建时间', sortable: true, halign: 'center'},
                {field: 'updateTime', title: '更新时间', sortable: true, halign: 'center'},
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
function actionFormatter(value, row, index) {
    return [
        '<a class="like" href="javascript:void(0)" data-toggle="tooltip" title="Like"><i class="glyphicon glyphicon-heart"></i></a>　',
        '<a class="edit ml10" href="javascript:void(0)" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
        '<a class="remove ml10" href="javascript:void(0)" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}

window.actionEvents = {
    'click .like': function (e, value, row, index) {
        alert('You click like icon, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    },
    'click .edit': function (e, value, row, index) {
        alert('You click edit icon, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    },
    'click .remove': function (e, value, row, index) {
        alert('You click remove icon, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    }
};

function detailFormatter(index, row) {
    var html = [];
    $.each(row, function (key, value) {
        html.push('<p><b>' + key + ':</b> ' + value + '</p>');
    });
    return html.join('');
}

// 新增
function createAction() {
    $.confirm({
        type: 'dark',
        animationSpeed: 300,
        title: '新增系统',
        content: $('#createDialog').html(),
        buttons: {
            confirm: {
                text: '确认',
                btnClass: 'waves-effect waves-button',
                action: function () {
                    $.alert('确认');
                }
            },
            cancel: {
                text: '取消',
                btnClass: 'waves-effect waves-button'
            }
        }
    });
}

// 编辑
function updateAction() {
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
            type: 'blue',
            animationSpeed: 300,
            title: '编辑系统',
            content: $('#createDialog').html(),
            buttons: {
                confirm: {
                    text: '确认',
                    btnClass: 'waves-effect waves-button',
                    action: function () {
                        $.alert('确认');
                    }
                },
                cancel: {
                    text: '取消',
                    btnClass: 'waves-effect waves-button'
                }
            }
        });
    }
}

// 删除
function deleteAction() {
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
            content: '确认删除该系统吗？',
            buttons: {
                confirm: {
                    text: '确认',
                    btnClass: 'waves-effect waves-button',
                    action: function () {
                        var ids = new Array();
                        for (var i in rows) {
                            ids.push(rows[i].systemId);
                        }
                        $.alert('删除：id=' + ids.join("-"));
                    }
                },
                cancel: {
                    text: '取消',
                    btnClass: 'waves-effect waves-button'
                }
            }
        });
    }
}
