'use strict';

angular.module('aclcrawlerApp').controller('BlacklistDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Blacklist',
        function($scope, $stateParams, $modalInstance, entity, Blacklist) {

        $scope.blacklist = entity;
        $scope.load = function(id) {
            Blacklist.get({blacklist_id : id}, function(result) {
                $scope.blacklist = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('aclcrawlerApp:blacklistUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.blacklist.blacklist_id != null) {
                Blacklist.update($scope.blacklist, onSaveFinished);
            } else {
                Blacklist.save($scope.blacklist, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
