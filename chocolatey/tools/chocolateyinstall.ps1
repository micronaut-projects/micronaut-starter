$version = '3.8.12'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C00A422CBD36F0AE2AF9DBDBBFB0408885EE7032BEBB20D40BBFEA13A2019758'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
