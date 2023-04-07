$version = '3.8.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'AC34FDE71C0FBD65E34C3A67CED378F269652CDE56A3F8DF16CFAC630CF8D4EC'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
