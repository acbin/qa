(function (window, undefined) {
    var Business = Base.createClass('main.util.Business');
    var Action = Base.getClass('main.util.Action');

    $.extend(Business, {
        followUser: fFollowUser,
        followQuestion: fFollowQuestion
    });

    function fFollowUser() {
        $(document).on('click', '.js-follow-user', function (oEvent) {
            var oEl = $(oEvent.currentTarget);
            var sId = $.trim(oEl.attr('data-id'));
            if (!sId) {
                return;
            }
            // 禁止频繁点击
            if (oEl.attr('data-limit')) {
                return;
            }
            oEl.attr('data-limit', '1');
            var bFollow = oEl.attr('data-status') === '1';
            Action[bFollow ? 'unFollowUser' : 'followUser']({
                userId: sId,
                call: function (oResult) {
                    if (oResult.code === 999) {
                        // 未登录
                        alert('未登录');
                        window.location.href = '/reglogin?next=' + window.encodeURI(window.location.href);
                        return;
                    } else if (oResult.code === 0) {
                        // 修改标记位
                        oEl.attr('data-status', bFollow ? '0' : '1');
                        // 其他操作
                    } else {
                        alert('出现错误，请重试');
                    }
                },
                error: function (oResult) {
                    alert('出现错误，请重试');
                },
                always: function () {
                    oEl.removeAttr('data-limit');
                }
            });
        });
    }

    function fFollowQuestion() {
        var that = this;
        $(document).on('click', 'js-follow-question', function (oEvent) {
            var oEl = $(oEvent.currentTarget);
            var sId = $.trim(oEl.attr('data-id'));
            if (!sId) {
                return;
            }
            // 禁止频繁点击
            if (oEl.attr('data-limit')) {
                return;
            }
            oEl.attr('data-limit', '1');
            var bFollow = oEl.attr('data-status') === '1';
            Action[bFollow ? 'unFollowQuestion' : 'followQuestion']({
                questionId: sId,
                call: function (oResult) {
                    if (oResult.code === 999) {
                        // 未登录
                        alert('未登录');
                        window.location.href = '/reglogin?next=' + window.encodeURI(window.location.href);
                        return;
                    } else if (oResult.code === 0) {
                        // 修改标记位
                        oEl.attr('data-status', bFollow ? '0' : '1');
                        // 其他操作
                    } else {
                        alert('出现错误，请重试');
                    }
                },
                error: function (oResult) {
                    alert('出现错误，请重试');
                },
                always: function () {
                    oEl.removeAttr('data-limit');
                }
            });
        });
    }
})(window);