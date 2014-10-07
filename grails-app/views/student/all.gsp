
<script src="/demo/js/student/StudentApp.js"></script>
<script src="/demo/js/student/StudentController.js"></script>

<meta name="layout" content="main"/>

# students: ${students.size()} <br/>

<div id='content' ng-app='StudentApp' ng-controller='StudentController'>
    <p>What?? {{understand}}</p>
    <input type='text' ng-model='inputValue' />
    {{inputValue}}
</div>