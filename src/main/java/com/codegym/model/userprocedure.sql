use quanlyuser;
DELIMITER  //
create procedure get_user_by_id(IN  user_id INT)
begin
    select name, email, country from user
        where id = user_id;
end //
DELIMITER ;

DELIMITER  //
create procedure insert_user (
IN user_name varchar(50),
IN user_email varchar(50),
IN user_country varchar(50)
)
begin
    insert into user (name, email, country) VALUES  (user_name, user_email, user_country);
end //
DELIMITER  ;

