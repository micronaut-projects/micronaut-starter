$version = '2.5.7'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6F96B79B121BC15AD46C1EE9316647A6354ACD173A620FA0B148992652C8BA17'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
