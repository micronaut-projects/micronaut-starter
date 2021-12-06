$version = '3.2.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '0EAC8AB59CAA5FF86F72B9DBD8FDBE822D890FA952D1D907BBB5F70D8EBE227A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
