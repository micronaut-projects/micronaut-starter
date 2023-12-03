$version = '4.2.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '2F9B908A532F5FD794209B1C1F76FC2EA963009F301CBC653D5AAA217610D4A1'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
