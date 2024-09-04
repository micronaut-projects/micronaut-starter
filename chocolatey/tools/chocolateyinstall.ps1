$version = '4.6.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '566DE546416AFDDC9AA3CF64548568FC3C8350079C18CFC2029854ECD33B7F2C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
