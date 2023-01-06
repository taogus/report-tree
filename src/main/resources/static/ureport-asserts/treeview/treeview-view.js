/**
 * 初始化树元素
 *
 * @param treeElId
 * @param treeData
 * @param valElId
 * @param iconElId
 */
function initTreeView(treeElId, treeData, valElId, iconElId) {
    let $optionTree = $(treeElId);
    $optionTree.treeview({
        levels: 3,//默认显示情况下  树显示到2级深度 默认是2
        data: JSON.parse(treeData),
        //showCheckbox: 1,//复选框设置，也可以是true
        showIcon: false,
        //onhoverColor: 'rgba(67,143,207,0.3)',
        selectedColor: '#000000',
        selectedBackColor: '#EEEEEE',
        collapseIcon:"",
        showTags: 0,//节点的右边显示附加信息 一般要在数据 加tags：[] 属性
        //enableLinks:1,//能给节点附加URL 要与数据里的 href 连用
        showBorder: true,
        onNodeSelected: function (evetn, node) {
            $(valElId).val(node.text);
            //$("#tree").hide();
            $(iconElId).toggleClass("glyphicon glyphicon-chevron-down").toggleClass("glyphicon glyphicon-chevron-up");

        }
    });
}

/**
 * 树下拉框点击
 *
 * @param treeElId
 * @param iconElId
 * @param event
 */
function treeViewclick(treeElId, iconElId, event) {
    $(treeElId).show();
    //切换图标
    $(iconElId).toggleClass("glyphicon glyphicon-chevron-down").toggleClass("glyphicon glyphicon-chevron-up");
    //阻止事件传播  防止传到document的点击事件中
    event.stopPropagation();
}