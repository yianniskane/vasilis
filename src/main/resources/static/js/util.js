function reverseDate(date){
    let dateOriginal = [];
    dateOriginal = date.split('-');

    return dateOriginal[2] + '-' + dateOriginal[1] + '-' + dateOriginal[0];
}

function formatDate(){
    let today = new Date();
    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0');
    let yyyy = today.getFullYear();

    return mm + '-' + dd + '-' + yyyy;
}

function errorText(jqXHR){
    let error_msg = '';

    switch (jqXHR.status) {
        case 0:
            error_msg = 'Not connected.\n Verify Network.';
            break;

        case 204:
            error_msg = 'No Content.';
            break;

        case 400:
            error_msg = 'Bad Request.';
            break;

        case 401:
            error_msg = 'Unauthorized.';
            break;

        case 404:
            error_msg = 'Requested page not found.';
            break;

        case 502:
            error_msg = 'Bad Gateway.';
            break;

        case 503:
            error_msg = 'Service Unavailable.';
            break;

        case 500:
            error_msg = 'Internal Server Error.';
            break;

        default:
            error_msg = 'Uncaught Error.\n' + jqXHR.responseText;
    }

    return error_msg;
}


function clearFields(){
    $('#contact_no').val('');
    $('#date_recorded').val('');
    $('#company').val('');
    $('#manager').val('');
    $('#email').val('');
    $('#telephone').val('');
    $('#fax').val('');
    $('#orders').val('');
    $('#comments').val('');
    $('#seller').val('');
    $('#last_comm_date').val('');
    $('#followup').val('');
    $('#afm').val('');
    $('#street').val('');
    $('#city').val('');
    $('#nomos').val('');
    $('#zip').val('');
    $('#area').val('');
    $('#from_date').val('');
    $('#until_date').val('');
    $('#searchfor').val('');
    // $('#dropDownList').prop('selectedIndex',0);
    $('#completed').prop('checked', false);
    $('#ignored').prop('checked', false);
}