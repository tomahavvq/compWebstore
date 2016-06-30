(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('UserInfoController', UserInfoController);

    UserInfoController.$inject = ['$scope', '$state', 'UserInfo'];

    function UserInfoController ($scope, $state, UserInfo) {
        var vm = this;
        
        vm.userInfos = [];

        loadAll();

        function loadAll() {
            UserInfo.query(function(result) {
                vm.userInfos = result;
            });
        }
    }
})();
