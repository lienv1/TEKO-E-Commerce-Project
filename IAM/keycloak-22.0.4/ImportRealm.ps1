# Get the local IPv4 address
$localIP = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

if ($null -eq $localIP) {
    Write-Host "No DHCP IPv4 address found"
    exit 1
}

try {
    # Construct the command with proper quoting
    $command = "/opt/keycloak/bin/kc.sh import --dir `"/opt/keycloak/data/exportedRealm`" --override false --db-url=jdbc:mysql://`"$localIP`":3306/Keycloak --db-username root --db-password my-secret-pw"

    # Run the command in the container
    docker exec -it keycloak-container /bin/bash -c $command
}
catch {
    Write-Host "An error occurred: $_"
    exit 1
}
