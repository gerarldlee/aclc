'use strict';

angular.module('aclcrawlerApp').controller('UsersDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Users', 'Password_history',
        function($scope, $stateParams, $modalInstance, entity, Users, Password_history) {

        $scope.users = entity;
        $scope.password_historys = Password_history.query();
        $scope.load = function(id) {
            Users.get({id : id}, function(result) {
                $scope.users = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('aclcrawlerApp:usersUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.users.id != null) {
                Users.update($scope.users, onSaveFinished);
            } else {
                Users.save($scope.users, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
