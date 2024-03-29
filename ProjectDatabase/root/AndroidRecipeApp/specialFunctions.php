<?php
//header('Content-Type: application/json');
include("dbConfig.php");
$data = "";
$array = [];
$teller = 0;
if (!empty ($_POST["tableName"])) {
    $tableName = $_POST["tableName"];
    if ($tableName == "ap_recipes_by_category") {
        if (!empty ($_POST["catId"])) {
            if (is_numeric($_POST["catId"])) {
                $id = $_POST["catId"];

                $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
                if (mysqli_connect_errno()) {
                    $array["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
                } else {
                    $sql = "SELECT * FROM $tableName WHERE `CategoryId` = ?";
                    $query = $con->prepare($sql);
                    if ($query === false) {
                        $array["error"] = "Failed to prepare the query: " . $con->error;
                    } else {
                        $bp = $query->bind_param("i", $id);
                        if ($bp === false) {
                            $array["error"] = "Failed to bind params: " . $query->error;
                        } else {
                            $exec = $query->execute();
                            if ($exec === false) {
                                $array["error"] = "Failed to execute the query: " . $query->error;
                            } else {
                                $meta = $query->result_metadata();
                                while ($field = $meta->fetch_field()) {
                                    $params[] = &$row[$field->name];
                                }
                                call_user_func_array(array($query, 'bind_result'), $params);
                                $fetch = $query->fetch();
                                if ($fetch === null) {
                                    $array["error"] = "Er zijn geen overeenkomstige rijen gevonden.";
                                } else {
                                    do {
                                        $array["result"][$teller] = $row;
                                        $teller++;
                                    } while ($query->fetch());
                                }
                            }
                        }
                        $query->close();
                    }
                    $con->close();
                }
            } else {
                $array["error"] = "ID is not a number";
            }
        }
    }else if($tableName == "ap_rating"){
        if (!empty ($_POST["ratingId"])) {
            if (is_numeric($_POST["ratingId"])) {
                $id = $_POST["ratingId"];

                $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
                if (mysqli_connect_errno()) {
                    $array["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
                } else {
                    $sql = "SELECT * FROM $tableName WHERE `RecipeId` = ?";
                    $query = $con->prepare($sql);
                    if ($query === false) {
                        $array["error"] = "Failed to prepare the query: " . $con->error;
                    } else {
                        $bp = $query->bind_param("i", $id);
                        if ($bp === false) {
                            $array["error"] = "Failed to bind params: " . $query->error;
                        } else {
                            $exec = $query->execute();
                            if ($exec === false) {
                                $array["error"] = "Failed to execute the query: " . $query->error;
                            } else {
                                $meta = $query->result_metadata();
                                while ($field = $meta->fetch_field()) {
                                    $params[] = &$row[$field->name];
                                }
                                call_user_func_array(array($query, 'bind_result'), $params);
                                $fetch = $query->fetch();
                                if ($fetch === null) {
                                    $array["error"] = "Er zijn geen overeenkomstige rijen gevonden.";
                                } else {
                                    do {
                                        $array["result"][$teller] = $row;
                                        $teller++;
                                    } while ($query->fetch());
                                }
                            }
                        }
                        $query->close();
                    }
                    $con->close();
                }
            } else {
                $array["error"] = "ID is not a number";
            }
        }
    }
} else {
    $array["error"] = "Tablename not found.";
}
print json_encode($array);
//$data = json_encode($array);
?>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>
<form action="specialFunctions.php" method="post">
    <input type="text" name="tableName" value="ap_recipe" />
    <input type="text" name="catId" value="" />
    <input type="submit" value="test"/>
</form>
</body>
</html>