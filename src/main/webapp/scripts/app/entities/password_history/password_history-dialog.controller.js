'use strict';

angular.module('aclcrawlerApp').controller('Password_historyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Password_history', 'Users',
        function($scope, $stateParams, $modalInstance, entity, Password_history, Users) {

        $scope.password_history = entity;
        $scope.userss = Users.query();
        $scope.load = function(id) {
            Password_history.get({id : id}, function(result) {
                $scope.password_history = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('aclcrawlerApp:password_historyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.password_history.id != null) {
                Password_history.update($scope.password_history, onSaveFinished);
            } else {
                Password_history.save($scope.password_history, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
