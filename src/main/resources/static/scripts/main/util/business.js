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
            Action[bFollow ? 'unfollowUser' : 'followUser']({
                userId: sId,
                call: function (oResult) {
                    if (oResult.code === 999) {
                        // 未登录
                        //alert('未登录');
                        window.location.href = '/reglogin?next=' + window.encodeURI(window.location.href);
                        return;
                    } else if (oResult.code === 0) {
                        // alert(bFollow);
                        // 修改标记位
                        oEl.attr('data-status', bFollow ? '0' : '1');

                        // 新增：其他操作
                        // 按钮颜色
                        oEl.removeClass('zg-btn-follow zg-btn-unfollow').addClass(bFollow ? 'zg-btn-follow' : 'zg-btn-unfollow');
                        // 文字
                        oEl.html(bFollow ? '关注' : '取消关注');
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

    function fFollowQuestion(oConf) {
        var that = this;
        var oCountEl = $(oConf.countEl);
        var oListEl = $(oConf.listEl);

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
            Action[bFollow ? 'unfollowQuestion' : 'followQuestion']({
                questionId: sId,
                call: function (oResult) {
                    if (oResult.code === 999) {
                        // 未登录
                        // alert('未登录');
                        window.location.href = '/reglogin?next=' + window.encodeURI(window.location.href);
                        return;
                    } else if (oResult.code === 0) {
                        // 修改标记位
                        oEl.attr('data-status', bFollow ? '0' : '1');

                        // 其他操作：新增
                        // 按钮颜色
                        oEl.removeClass('zg-btn-white zg-btn-green').addClass(bFollow ? 'zg-btn-green' : 'zg-btn-white');
                        // 文字
                        oEl.html(bFollow ? '关注问题' : '取消关注');
                        // 修改数量
                        oCountEl.html(oResult.count);
                        if (bFollow) {
                            // 移除用户
                            oListEl.find('.js-user-' + oResult.id).remove();
                        } else {
                            // 显示用户
                            oListEl.prepend('<a class="zm-item-link-avatar js-user-' + oResult.id + '" href="/user/' + oResult.id + '" data-original_title="' + oResult.name + '"><img src="' + oResult.headUrl + '" class="zm-item-img-avatar"></a>');
                        }


                    } else {
                        alert('出现错误，请重试1');
                    }
                },
                error: function (oResult) {
                    alert('出现错误，请重试2');
                },
                always: function () {
                    oEl.removeAttr('data-limit');
                }
            });
        });
    }
})(window);