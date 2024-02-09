$version = '4.3.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '749EC26284AABE81B787B29373A38C2E618BD01E9DFC395097B4DB6F5F3219B9'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
